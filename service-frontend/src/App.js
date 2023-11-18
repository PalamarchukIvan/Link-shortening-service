import './App.css';
import HeaderComponent from "./components/HeaderComponent";
import FooterComponent from "./components/FooterComponent";
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'
import RegLogComponent from "./components/RegLogComponent";
import MainPageComponent from "./components/MainPageComponent";
import CreateShortLinkComponent from "./components/CreateShortLinkComponent";

function App() {
  return (
      <div>
          <Router>
              <HeaderComponent/>
              <div className="container">
                  <Switch>
                      <Route path="/create-short-link"  component={CreateShortLinkComponent}></Route>
                      <Route path="/main"  component={MainPageComponent}></Route>
                      <Route path="/" exact component={RegLogComponent}></Route>
                  </Switch>
              </div>
              <FooterComponent/>
          </Router>
      </div>
  );
}

export default App;
