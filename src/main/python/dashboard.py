import streamlit as st
import pandas as pd
import requests
import plotly.express as px
from datetime import datetime
import os
import time
import logging

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - [TRANSACTION] - %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)
logger = logging.getLogger(__name__)

print("STATUS: RUNNING")
print("URL: http://localhost:8501")

st.set_page_config(page_title="Pro Team Roster Dashboard", layout="wide")

def load_css():
    """Loads custom CSS from the style.css file."""
    css_path = os.path.join(os.path.dirname(__file__), "style.css")
    if os.path.exists(css_path):
        with open(css_path) as f:
            st.markdown(f'<style>{f.read()}</style>', unsafe_allow_html=True)

load_css()

# Constants - Dynamically discover backend URL
BASE_URL = os.getenv("BACKEND_URL", "http://localhost:8080")

def discover_backend_port():
    """Attempt to discover the actual backend port from the /health/port endpoint."""
    max_retries = 10
    retry_delay = 1  # 1 second

    for attempt in range(1, max_retries + 1):
        try:
            response = requests.get(f"{BASE_URL}/health/port", timeout=5)
            if response.status_code == 200:
                data = response.json()
                if 'url' in data:
                    discovered_url = data['url']
                    print(f"[Dashboard] Backend discovered at: {discovered_url}")
                    return discovered_url
        except Exception as e:
            if attempt < max_retries:
                print(f"[Dashboard] Attempt {attempt}/{max_retries} - Backend not ready. Retrying in {retry_delay}s...")
                time.sleep(retry_delay)

    print(f"[Dashboard] Could not discover backend port. Using default: {BASE_URL}")
    return BASE_URL

# Discover backend on app load
BASE_URL = discover_backend_port()

@st.cache_data(ttl=60)
def fetch_data(endpoint, params=None):
    try:
        response = requests.get(f"{BASE_URL}/{endpoint}", params=params, timeout=5)
        if response.status_code == 200:
            return response.json()
    except Exception as e:
        return None
    return None

def get_health():
    data = fetch_data("health")
    if data:
        return data.get("status", "DOWN")
    return "OFFLINE"

def render_mascot_image(mascot, caption=None):
    """Renders a mascot image with consistent URL handling and accessibility."""
    if not mascot:
        return
    image_url = mascot.get('imageUrl')
    # If no caption provided, use a descriptive one for ADA compliance
    if not caption:
        caption = f"Official Mascot: {mascot.get('name', 'Mascot')} - a {mascot.get('species', 'friendly creature')}"
    
    if image_url:
        if image_url.startswith('/'):
            image_url = f"{BASE_URL}{image_url}"
        st.image(image_url, caption=caption, width="stretch")
    else:
        st.info(f"Image for {mascot.get('name', 'this mascot')} is currently unavailable.")

# Sidebar
st.sidebar.title("🏀 Pro Team Roster")
health_status = get_health()
if health_status == "UP":
    st.sidebar.success(f"Backend Status: {health_status}")
elif health_status == "OFFLINE":
    st.sidebar.error(f"Backend Status: {health_status}")
else:
    st.sidebar.warning(f"Backend Status: {health_status}")

st.sidebar.markdown("---")
st.sidebar.header("Navigation")
page = st.sidebar.radio("Go to", ["Team Analytics", "Arena Overview", "Schedule Explorer"])
logger.info(f"User navigated to: {page}")

st.title("🏀 Pro Team Roster Analytics")

if health_status == "OFFLINE":
    st.warning(f"Please ensure the Spring Boot backend is running (currently trying {BASE_URL})")
    st.info("Tip: Run `mvn spring-boot:run` to start the backend.")

teams_data = fetch_data("teams")
teams_df = pd.DataFrame(teams_data) if teams_data else pd.DataFrame()

if page == "Team Analytics":
    if not teams_df.empty:
        selected_team = st.sidebar.selectbox("Select a Team", teams_df['name'].unique())
        logger.info(f"Team selected: {selected_team}")
        team_data = teams_df[teams_df['name'] == selected_team].iloc[0]
        team_id = team_data['teamId']

        st.header(f"Team: {selected_team}")
        
        # Team Metrics in a 2x2 grid to prevent field name cutoff
        mcol1, mcol2 = st.columns(2)
        mcol1.metric("City", team_data['city'])
        mcol2.metric("Founded", team_data['foundedYear'])
        
        mcol3, mcol4 = st.columns(2)
        mcol3.metric("Championships", team_data['championships'])
        mcol4.metric("Owner", team_data['owner'])

        # Mascot Section
        st.subheader("🐾 Team Mascot")
        # Use timestamp to bypass cache for mascot data
        timestamp = datetime.now().strftime("%Y%m%d%H%M%S")
        mascot_data = fetch_data("mascots/team", params={"team-id": team_id, "t": timestamp})
        if mascot_data and len(mascot_data) > 0:
            m = mascot_data[0]
            mcol1, mcol2 = st.columns([1, 2])
            with mcol1:
                render_mascot_image(m)
            with mcol2:
                st.markdown(f"**Name:** {m['name']}")
                st.markdown(f"**Species:** {m['species']}")
                st.markdown(f"**Personality:** {m['personality']}")
                st.info(f"**Description:** {m['description']}")
        else:
            st.write("No mascot data available.")

        # Random Mascot Spotlight
        st.sidebar.markdown("---")
        st.sidebar.subheader("🌟 Mascot Spotlight")
        if st.sidebar.button("Fetch Random Mascot"):
            logger.info("Fetching random mascot spotlight")
            # Use timestamp to bypass cache for random mascot
            timestamp = datetime.now().strftime("%Y%m%d%H%M%S")
            random_m = fetch_data("mascots/random", params={"t": timestamp})
            if random_m:
                with st.sidebar:
                    render_mascot_image(random_m, caption=f"{random_m['name']} ({random_m['species']})")
                    st.write(f"*{random_m['personality']}*")
            else:
                st.sidebar.error("Failed to fetch mascot")

        # Roster Section
        st.subheader("📋 Roster Details")
        roster_data = fetch_data(f"teams/{team_id}/roster")
        if roster_data:
            players = roster_data.get('proPlayers', [])
            players_df = pd.DataFrame(players)
            
            if not players_df.empty:
                st.dataframe(
                    players_df[['name', 'position', 'age', 'height', 'weight', 'salary', 'jerseyNumber', 'injuryStatus']],
                    width="stretch",
                    column_config={
                        "name": st.column_config.TextColumn("Player Name", width="medium"),
                        "position": st.column_config.TextColumn("Position", width="small"),
                        "age": st.column_config.NumberColumn("Age"),
                        "height": st.column_config.TextColumn("Height", width="small"),
                        "weight": st.column_config.TextColumn("Weight", width="small"),
                        "salary": st.column_config.NumberColumn("Salary", format="$%d"),
                        "jerseyNumber": st.column_config.NumberColumn("Jersey #"),
                        "injuryStatus": st.column_config.TextColumn("Injury Status", width="medium"),
                    }
                )

                st.subheader("📊 Visual Analytics")
                vcol1, vcol2 = st.columns(2)

                with vcol1:
                    fig_age = px.histogram(
                        players_df, 
                        x="age", 
                        title="Age Distribution", 
                        nbins=10,
                        color="name",
                    )
                    # Increase Y-axis range for better visibility
                    if not players_df.empty and players_df['age'].nunique() > 1:
                        max_age_count = players_df.groupby(pd.cut(players_df['age'], bins=10)).size().max()
                    else:
                        max_age_count = len(players_df) if not players_df.empty else 5

                    fig_age.update_layout(
                        yaxis_range=[0, max_age_count * 3],
                        plot_bgcolor='rgba(0,0,0,0)',
                        paper_bgcolor='rgba(0,0,0,0)',
                        font_color='#e0e0e0',
                        margin=dict(l=20, r=20, t=40, b=20)
                    )
                    st.plotly_chart(fig_age, width="stretch")

                with vcol2:
                    fig_salary = px.bar(
                        players_df, 
                        x="name", 
                        y="salary", 
                        color="position", 
                        title="Player Salaries",
                        color_discrete_sequence=["#00d4ff", "#ff4b2b", "#007bff", "#ff8c00"]
                    )
                    # Increase Y-axis range for better visibility
                    max_salary = players_df['salary'].max() if not players_df.empty else 1000000
                    fig_salary.update_layout(
                        yaxis_range=[0, max_salary * 1.15],
                        plot_bgcolor='rgba(0,0,0,0)',
                        paper_bgcolor='rgba(0,0,0,0)',
                        font_color='#e0e0e0',
                        margin=dict(l=20, r=20, t=40, b=20)
                    )
                    st.plotly_chart(fig_salary, width="stretch")
                
                fig_hw = px.scatter(
                    players_df, 
                    x="height", 
                    y="weight", 
                    color="position", 
                    size="salary", 
                    hover_name="name", 
                    title="Height vs Weight (Size = Salary)",
                    color_discrete_sequence=["#00d4ff", "#ff4b2b", "#007bff", "#ff8c00"]
                )
                fig_hw.update_layout(
                    plot_bgcolor='rgba(0,0,0,0)',
                    paper_bgcolor='rgba(0,0,0,0)',
                    font_color='#e0e0e0'
                )
                st.plotly_chart(fig_hw, width="stretch")
    else:
        st.info("No teams found. Start the backend to see live data.")

elif page == "Arena Overview":
    st.header("🏟️ Arena Overview")
    arenas_data = fetch_data("arenas")
    if arenas_data:
        arenas_df = pd.DataFrame(arenas_data)
        st.dataframe(
            arenas_df[['name', 'location', 'capacity', 'openedYear', 'surface', 'cost']],
            width="stretch",
            column_config={
                "name": st.column_config.TextColumn("Arena Name", width="medium"),
                "location": st.column_config.TextColumn("Location", width="medium"),
                "capacity": st.column_config.NumberColumn("Capacity", format="%d"),
                "openedYear": st.column_config.NumberColumn("Opened", format="%d"),
                "surface": st.column_config.TextColumn("Surface", width="small"),
                "cost": st.column_config.NumberColumn("Build Cost", format="$%d"),
            }
        )
        
        st.subheader("Capacity Comparison")
        fig_cap = px.bar(
            arenas_df, 
            x="name", 
            y="capacity", 
            color="location", 
            title="Arena Capacity by Location",
            color_discrete_sequence=["#00d4ff", "#ff4b2b", "#007bff", "#ff8c00"]
        )
        # Increase Y-axis range for better visibility
        max_cap = arenas_df['capacity'].max() if not arenas_df.empty else 50000
        fig_cap.update_layout(
            yaxis_range=[0, max_cap * 1.15],
            plot_bgcolor='rgba(0,0,0,0)',
            paper_bgcolor='rgba(0,0,0,0)',
            font_color='#e0e0e0'
        )
        st.plotly_chart(fig_cap, width="stretch")
        
        st.subheader("Construction Cost Analysis")
        fig_cost = px.pie(
            arenas_df, 
            values='cost', 
            names='name', 
            title='Arena Construction Cost Distribution',
            color_discrete_sequence=["#00d4ff", "#ff4b2b", "#007bff", "#ff8c00"]
        )
        fig_cost.update_layout(
            plot_bgcolor='rgba(0,0,0,0)',
            paper_bgcolor='rgba(0,0,0,0)',
            font_color='#e0e0e0'
        )
        st.plotly_chart(fig_cost, width="stretch")
    else:
        st.info("No arena data available.")

elif page == "Schedule Explorer":
    st.header("📅 Upcoming Games")
    schedules_data = fetch_data("schedules")
    if schedules_data:
        # Flattening nested data for display
        flat_schedules = []
        for s in schedules_data:
            flat_schedules.append({
                "Date": s['scheduledDate'],
                "Home Team": s['homeTeam']['name'] if s['homeTeam'] else "N/A",
                "Away Team": s['awayTeam']['name'] if s['awayTeam'] else "N/A",
                "Arena": s['arena']['name'] if s['arena'] else "N/A",
                "Ticket Price": s['ticketPrice'],
                "Location": s['arena']['location'] if s['arena'] else "N/A"
            })
        sched_df = pd.DataFrame(flat_schedules)
        st.dataframe(
            sched_df.sort_values("Date"),
            width="stretch",
            column_config={
                "Date": st.column_config.TextColumn("Scheduled Date", width="medium"),
                "Home Team": st.column_config.TextColumn("Home Team", width="medium"),
                "Away Team": st.column_config.TextColumn("Away Team", width="medium"),
                "Arena": st.column_config.TextColumn("Arena", width="medium"),
                "Ticket Price": st.column_config.NumberColumn("Ticket Price", format="$%d"),
                "Location": st.column_config.TextColumn("Location", width="medium"),
            }
        )
        
        st.subheader("Ticket Pricing Trends")
        sorted_sched = sched_df.sort_values("Date")
        fig_price = px.line(
            sorted_sched, 
            x="Date", 
            y="Ticket Price", 
            markers=True, 
            title="Ticket Prices over Time",
            color_discrete_sequence=["#ff4b2b"]
        )
        # Increase Y-axis range for better visibility
        max_price = sorted_sched['Ticket Price'].max() if not sorted_sched.empty else 200
        fig_price.update_layout(
            yaxis_range=[0, max_price * 1.15],
            plot_bgcolor='rgba(0,0,0,0)',
            paper_bgcolor='rgba(0,0,0,0)',
            font_color='#e0e0e0'
        )
        st.plotly_chart(fig_price, width="stretch")
    else:
        st.info("No schedule data available.")

st.sidebar.markdown("---")
st.sidebar.info("Explore team statistics and player distributions in a visually cool way!")
