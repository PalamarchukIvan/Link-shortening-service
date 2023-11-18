import React, {Component} from 'react';
import ShortLinkService from "../services/ShortLinkService";
import {withRouter} from "react-router-dom";

class MainPageComponent extends Component {
    constructor(props) {
        super(props);
        
        this.state = {
            shortLinks: []
        }
        this.addShortLink = this.addShortLink.bind(this)
    }
    componentDidMount() {
        ShortLinkService.getCurrentUserShortLinks().then((res) => {
           this.setState({
               shortLinks: res.data
           }) 
        });
    }
    
    addShortLink() {
        console.log('Button clicked!');
        this.props.history.push('/create-short-link')
    }
    
    render() {
        return (
            <div>
                <div className="col-md-12">
                    <h2 className="text-center">User Profile</h2>
                    
                    <div>
                        <button className="btn btn-primary" onClick={this.addShortLink.bind(this)}>Create new Short Link</button>
                    </div>
                    <br/>
                    <div className="row">
                        
                        <table className="table table-striped table-bordered">
                            <thead>
                            <tr>
                                <th className="text-center">hash</th>
                                <th className="text-center">link</th>
                                <th className="text-center">action</th>
                            </tr>
                            </thead>
                            <tbody>
                            {this.state.shortLinks.map(dataEntry => (
                                <tr key={dataEntry.hash}>
                                    <td className="text-center">{dataEntry.hash}</td>
                                    <td className="text-center">{dataEntry.link}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        );
    }
}

export default MainPageComponent;