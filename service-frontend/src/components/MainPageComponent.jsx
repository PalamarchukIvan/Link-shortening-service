import React, {Component} from 'react';
import ShortLinkService from "../services/ShortLinkService";
import {withRouter} from "react-router-dom";

class MainPageComponent extends Component {
    constructor(props) {
        super(props);
        
    }

    buttonClicked() {
        console.log('Button clicked!');
    }
    deleteButtonClicked(hash) {
        ShortLinkService.deleteShortLink(hash).then((res) => {
            this.getShortLinks()
        })
    }
    
    render() {
        return (
            <div>
                <div className="col-md-12">
                    <h2 className="text-center">User Profile</h2>
                    
                    <a href="/short-links">view links</a>
                </div>
            </div>
        );
    }
}

export default MainPageComponent;