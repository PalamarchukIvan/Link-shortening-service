import React, {Component} from 'react';
import ShortLinkService from "../services/ShortLinkService";

class ShortLinkComponent extends Component {
    constructor(props) {
        super(props);
        
        this.state = {
            shortLinks: []
        }
    }
    componentDidMount() {
        ShortLinkService.getCurrentUserShortLinks().then((res) => {
           this.setState({
               shortLinks: res.data
           }) 
        });
    }

    render() {
        return (
            <div className="container">
                <div className="row">
                    <div className="col-md-12">
                        <h2 className="text-center">User data title</h2>
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

export default ShortLinkComponent;