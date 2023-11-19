import './App.css';
import HeaderComponent from "./components/HeaderComponent";
import FooterComponent from "./components/FooterComponent";
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'
import MainPageComponent from "./components/MainPageComponent";
import CreateShortLinkComponent from "./components/CreateShortLinkComponent";
import GreetingComponent from "./components/GreetingComponent";
import LoginComponent from "./components/LoginComponent";

function App() {
  return (
      <div>
          <Router>
              <HeaderComponent/>
              <div className="container">
                  <Switch>
                      <Route path="/create-short-link"  component={CreateShortLinkComponent}></Route>
                      <Route path="/main"  component={MainPageComponent}></Route>
                      <Route path="/login" exact component={LoginComponent}></Route>
                      <Route path="/" exact component={GreetingComponent}></Route>
                  </Switch>
              </div>
              <FooterComponent/>
          </Router>
      </div>
  );
}

export default App;
