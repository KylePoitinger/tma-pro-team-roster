import streamlit as st
import pandas as pd
import requests
import plotly.express as px
from datetime import datetime

st.set_page_config(page_title="Pro Team Roster Dashboard", layout="wide")

# Constants
BASE_URL = "http://localhost:8080"

@st.cache_data(ttl=60)
def fetch_data(endpoint):
    try:
        response = requests.get(f"{BASE_URL}/{endpoint}")
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

st.title("🏀 Pro Team Roster Analytics")

if health_status == "OFFLINE":
    st.warning("Please ensure the Spring Boot backend is running at http://localhost:8080")
    st.info("Tip: Run `mvn spring-boot:run` to start the backend.")

teams_data = fetch_data("teams")
teams_df = pd.DataFrame(teams_data) if teams_data else pd.DataFrame()

if page == "Team Analytics":
    if not teams_df.empty:
        selected_team = st.sidebar.selectbox("Select a Team", teams_df['name'].unique())
        team_data = teams_df[teams_df['name'] == selected_team].iloc[0]
        team_id = team_data['teamId']

        st.header(f"Team: {selected_team}")
        
        col1, col2, col3, col4 = st.columns(4)
        col1.metric("City", team_data['city'])
        col2.metric("Founded", team_data['foundedYear'])
        col3.metric("Championships", team_data['championships'])
        col4.metric("Owner", team_data['owner'])

        # Mascot Section
        st.subheader("🐾 Team Mascot")
        mascot_data = fetch_data(f"mascots/team?team-id={team_id}")
        if mascot_data and len(mascot_data) > 0:
            m = mascot_data[0]
            mcol1, mcol2, mcol3 = st.columns(3)
            mcol1.markdown(f"**Name:** {m['name']}")
            mcol2.markdown(f"**Species:** {m['species']}")
            mcol3.markdown(f"**Personality:** {m['personality']}")
            st.info(f"**Description:** {m['description']}")
        else:
            st.write("No mascot data available.")

        # Roster Section
        st.subheader("📋 Roster Details")
        roster_data = fetch_data(f"teams/{team_id}/roster")
        if roster_data:
            players = roster_data.get('proPlayers', [])
            players_df = pd.DataFrame(players)
            
            if not players_df.empty:
                st.dataframe(players_df[['name', 'position', 'age', 'height', 'weight', 'salary', 'jerseyNumber', 'injuryStatus']])

                st.subheader("📊 Visual Analytics")
                vcol1, vcol2 = st.columns(2)

                with vcol1:
                    fig_age = px.histogram(players_df, x="age", title="Age Distribution", color_discrete_sequence=['#636EFA'])
                    st.plotly_chart(fig_age, use_container_width=True)

                with vcol2:
                    fig_salary = px.bar(players_df, x="name", y="salary", color="position", title="Player Salaries")
                    st.plotly_chart(fig_salary, use_container_width=True)
                
                fig_hw = px.scatter(players_df, x="height", y="weight", color="position", 
                                    size="salary", hover_name="name", title="Height vs Weight (Size = Salary)")
                st.plotly_chart(fig_hw, use_container_width=True)
    else:
        st.info("No teams found. Start the backend to see live data.")

elif page == "Arena Overview":
    st.header("🏟️ Arena Overview")
    arenas_data = fetch_data("arenas")
    if arenas_data:
        arenas_df = pd.DataFrame(arenas_data)
        st.dataframe(arenas_df[['name', 'location', 'capacity', 'openedYear', 'surface', 'cost']])
        
        st.subheader("Capacity Comparison")
        fig_cap = px.bar(arenas_df, x="name", y="capacity", color="location", title="Arena Capacity by Location")
        st.plotly_chart(fig_cap, use_container_width=True)
        
        st.subheader("Construction Cost Analysis")
        fig_cost = px.pie(arenas_df, values='cost', names='name', title='Arena Construction Cost Distribution')
        st.plotly_chart(fig_cost, use_container_width=True)
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
        st.dataframe(sched_df.sort_values("Date"))
        
        st.subheader("Ticket Pricing Trends")
        fig_price = px.line(sched_df.sort_values("Date"), x="Date", y="Ticket Price", markers=True, title="Ticket Prices over Time")
        st.plotly_chart(fig_price, use_container_width=True)
    else:
        st.info("No schedule data available.")

st.sidebar.markdown("---")
st.sidebar.info("Explore team statistics and player distributions in a visually cool way!")
