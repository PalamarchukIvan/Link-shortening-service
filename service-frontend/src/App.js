import logo from './logo.svg';
import './App.css';
import ShortLinkComponent from "./components/ShortLinkComponent";
import HeaderComponent from "./components/HeaderComponent";
import FooterComponent from "./components/FooterComponent";
import {BrowserRouter as router, Route, Switch} from 'react-router-dom'

function App() {
  return (
      <div>
          <Router>
              <div className="container">
                  <HeaderComponent/>
                  <div className="container">
                      <Switch>
                          <Route path="/" component={ShortLinkComponent}></Route>
                          <ShortLinkComponent/>
                      </Switch>
                  </div>
                  <FooterComponent/>
              </div>
          </Router>
      </div>
  );
}

export default App;
