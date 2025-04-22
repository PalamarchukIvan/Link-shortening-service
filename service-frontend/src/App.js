import './App.css';
import {BrowserRouter as Router, Redirect, Route, Switch} from 'react-router-dom'
import MainPageComponent from "./components/MainPageComponent";
import CreateShortLinkComponent from "./components/CreateShortLinkComponent";
import GreetingComponent from "./components/GreetingComponent";
import LoginComponent from "./components/LoginComponent";
import ShortLinksComponent from "./components/ShortLinksComponent";
import StatisticsComponent from "./components/StatisticsComponent";
import React from "react";
import AdminStatisticsComponent from "./components/AdminStatisticsComponent";
import EmailVerificationComponent from "./components/EmailVerificationComponent";
import RegistrationComponent from "./components/RegistrationComponent";
import PrivateRoute from "./components/util/PrivateRoute";
import HeaderComponent from "./components/util/HeaderComponent";
import FooterComponent from "./components/util/FooterComponent";

function App() {
  return (
      <div>
          <Router>
              <div style={{ overflowY: 'auto', overflowX: 'auto', minHeight: '100vh', position: 'relative' }}>
                  <HeaderComponent />
                  <div className="container">
                      <Switch>
                          {/* Public */}
                          <Route path="/"                   exact component={GreetingComponent} />
                          <Route path="/login"              exact component={LoginComponent} />
                          <Route path="/register"           exact component={RegistrationComponent} />
                          <Route path="/verify"             exact component={EmailVerificationComponent} />

                          {/* Protected */}
                          <PrivateRoute path="/main"                exact component={MainPageComponent} />
                          <PrivateRoute path="/create-short-link"   exact component={CreateShortLinkComponent} />
                          <PrivateRoute path="/short-links"         exact component={ShortLinksComponent} />
                          <PrivateRoute path="/user-stat/statistic" exact component={StatisticsComponent} />
                          <PrivateRoute path="/user-stat/profile"   exact component={MainPageComponent} />
                          <PrivateRoute path="/all-users-statistic" exact component={AdminStatisticsComponent} />

                          {/* Fallback */}
                          <Route render={() => <Redirect to="/" />} />
                      </Switch>
                  </div>
                  <FooterComponent />
              </div>
          </Router>
      </div>
  );
}

export default App;
