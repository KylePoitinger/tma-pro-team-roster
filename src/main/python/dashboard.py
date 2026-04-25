import streamlit as st
import pandas as pd
import requests
import plotly.express as px

st.set_page_config(page_title="Pro Team Roster Dashboard", layout="wide")

st.title("🏀 Pro Team Roster Analytics")
st.markdown("Explore team statistics and player distributions in a visually cool way!")

# Constants
BASE_URL = "http://localhost:8080"

@st.cache_data
def fetch_teams():
    try:
        response = requests.get(f"{BASE_URL}/teams")
        if response.status_code == 200:
            return pd.DataFrame(response.json())
    except:
        return pd.DataFrame()

teams_df = fetch_teams()

if not teams_df.empty:
    st.sidebar.header("Filter Options")
    selected_team = st.sidebar.selectbox("Select a Team", teams_df['name'].unique())

    # Get team data
    team_data = teams_df[teams_df['name'] == selected_team].iloc[0]
    team_id = team_data['teamId']

    col1, col2, col3 = st.columns(3)
    col1.metric("City", team_data['city'])
    col2.metric("Founded", team_data['foundedYear'])
    col3.metric("Championships", team_data['championships'])

    # Fetch Roster
    try:
        roster_response = requests.get(f"{BASE_URL}/teams/{team_id}/roster")
        if roster_response.status_code == 200:
            roster = roster_response.json().get('proPlayers', [])
            players_df = pd.DataFrame(roster)
            
            if not players_df.empty:
                st.subheader(f"{selected_team} Roster Details")
                st.dataframe(players_df[['name', 'position', 'age', 'height', 'weight', 'salary']])

                # Visualizations
                st.subheader("Visual Analytics")
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

    except Exception as e:
        st.error(f"Could not fetch roster data: {e}")

else:
    st.info("Please ensure the Spring Boot backend is running at http://localhost:8080")
    st.markdown("""
    ### Sample Data Visualization (Static Demo)
    If the backend is not running, here is what you could see:
    - **Interactive Histograms** for age and physical stats.
    - **Salary Breakdowns** by position.
    - **Scatter Plots** correlating physical traits with compensation.
    """)

st.sidebar.markdown("---")
st.sidebar.info("Tip: Run the Java app first to see live data!")
