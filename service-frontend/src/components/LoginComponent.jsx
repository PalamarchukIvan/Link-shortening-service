import React, {Component} from 'react';
import UserService from "../services/UserService";

class LoginComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: ''
        }
        this.changeUsernameHandler = this.changeUsernameHandler.bind(this)
        this.changePasswordHandler = this.changePasswordHandler.bind(this)
    }
    changeUsernameHandler= (event) => {
        this.setState({username: event.target.value});
    }
    changePasswordHandler= (event) => {
        this.setState({password: event.target.value});
    }
    signIn = (event) => {
        let user = {username: this.state.username, password: this.state.password}
        console.log("user => " + user.username + " " + user.password)
        let logined = UserService.doLogin(user)
        console.log("if logined " + logined)
    }
    registration = (event) => {
        console.log("clicked on reg")
    }
    render() {
        return (
            <div>
                <form>
                    <br/>
                    <div><label> UserName </label> 
                        <input placeholder="username" type="text" name="username" 
                               value={this.state.username} onChange={this.changeUsernameHandler}/>
                    </div>
                    <div><label> Password:</label>
                        <input placeholder="password" type="password" name="password"
                               value={this.state.password} onChange={this.changePasswordHandler}/>
                    </div>
                    <br/>
                    <button className="btn btn-success" onClick={this.signIn}>Sign in</button>
                    <button className="btn btn-primary" onClick={this.registration}>I don't have an account</button>
                </form>
            </div>
        );
    }
}

export default LoginComponent;