import logo from './logo.svg';
import './App.css';
import ShortLinkComponent from "./components/ShortLinkComponent";
import HeaderComponent from "./components/HeaderComponent";
import FooterComponent from "./components/FooterComponent";
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'
import RegLogComponent from "./components/RegLogComponent";

function App() {
  return (
      <div>
          <Router>
              <HeaderComponent/>
              <div className="container">
                  <div className="container">
                      <Switch>
                          <Route path="/" component={RegLogComponent}></Route>
                          <RegLogComponent/>
                      </Switch>
                  </div>
              </div>
              <FooterComponent/>
          </Router>
      </div>
  );
}

export default App;
