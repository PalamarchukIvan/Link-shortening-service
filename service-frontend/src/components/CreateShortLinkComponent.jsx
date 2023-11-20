import React, {Component} from 'react';
import {withRouter} from "react-router-dom";
import ShortLinkService from "../services/ShortLinkService";

const SHORT_LINK_REDIRECT_API = "http://localhost:8080/s/"
class CreateShortLinkComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isInvisible: true,
            hash: '',
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

        console.log(this.state.isInvisible)
        console.log(this.state.hash)
        let shortLink = {link: this.state.link}

        if(this.state.link == null || this.state.link === '') {
            return
        }
        ShortLinkService.createShortLink(shortLink).then(res => {
            console.log(res)
            if (res.config.url !== res.request.responseURL) {
                console.log('url2 => ' + res.config.url )
                console.log('url3 => ' + res.request.responseURL)
                document.location = res.request.responseURL
            } else {
                this.setState( {
                    hash: res.data.hash,
                    isInvisible: false,
                    link: ''
                })
                // document.location = "/main"
            }
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
                                        { !this.state.isInvisible&&<a href={SHORT_LINK_REDIRECT_API + this.state.hash}>{SHORT_LINK_REDIRECT_API + this.state.hash}</a>}
                                        <br/>
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