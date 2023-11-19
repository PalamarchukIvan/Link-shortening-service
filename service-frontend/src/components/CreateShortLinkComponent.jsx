import React, {Component} from 'react';
import {withRouter} from "react-router-dom";
import ShortLinkService from "../services/ShortLinkService";

class CreateShortLinkComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            link: ''
        }
        this.cancel = this.cancel.bind(this)
        this.changeLinkHandler = this.changeLinkHandler.bind(this);
        this.createNewShortLink = this.createNewShortLink.bind(this);
    }
    changeLinkHandler= (event) => {
        this.setState({link: event.target.value});
    }
    createNewShortLink= (event) => {
        event.preventDefault()
        
        let shortLink = {link: this.link}
        console.log('new link => ' + shortLink)
        
        ShortLinkService.createShortLink(shortLink).then(res => {
            this.props.history.push("/main")
        })
    }
    cancel() {
        this.props.history.push('/main')
    }
    render() {
        return (
            <div>
                <div>
                    <br></br>
                    <div className = "container">
                        <div className = "row">
                            <div className = "card col-md-6 offset-md-3 offset-md-3">   
                                <div className = "card-body">
                                    <form>
                                        <div className = "form-group">
                                            <label> Link: </label>
                                            <input placeholder="Link" name="link" className="form-control"
                                                   value={this.state.link} onChange={this.changeLinkHandler}/>
                                        </div>

                                        <button className="btn btn-success" onClick={this.createNewShortLink}>Save</button>
                                        <button className="btn btn-danger" onClick={this.cancel} style={{marginLeft: "10px"}}>Cancel</button>
                                    </form>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        );
    }
}

export default CreateShortLinkComponent;