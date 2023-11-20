import React, { Component } from 'react';
import ShortLinkService from '../services/ShortLinkService';

const SHORT_LINK_REDIRECT_API = "http://localhost:8080/s/"
class ShortLinksComponent extends Component {
    constructor(props) {
        super(props);

        this.state = {
            shortLinks: [],
        };
        this.buttonClicked = this.buttonClicked.bind(this);
        this.deleteButtonClicked = this.deleteButtonClicked.bind(this);
    }

    async componentDidMount() {
        await this.getShortLinks();
    }

    async getShortLinks() {
        const res = await ShortLinkService.getCurrentUserShortLinks().then((res) => {
            return res;
        });

        if (res.config.url !== res.request.responseURL) {
            document.location = res.request.responseURL;
        } else {
            this.setState({
                shortLinks: res.data,
            });
        }
    }

    buttonClicked() {
        console.log('Button clicked!');
    }

    deleteButtonClicked(hash) {
        ShortLinkService.deleteShortLink(hash).then((res) => {
            this.getShortLinks();
        });
    }

    render() {
        return (
            <div>
                <div>
                    <a href="/create-short-link" className="btn btn-primary" onClick={this.buttonClicked.bind(this)}>
                        Create new Short Link
                    </a>
                </div>
                <br />
                <div className="row">
                    <div className="table-container" style={{ maxHeight: '400px', overflow: 'auto' }}>
                        <table className="table table-striped table-bordered">
                            <thead>
                            <tr>
                                <th className="text-center">shorten-link</th>
                                <th className="text-center">link</th>
                                <th className="text-center">action</th>
                            </tr>
                            </thead>
                            <tbody>
                            {this.state.shortLinks.map((dataEntry) => (
                                <tr key={dataEntry.hash}>
                                    
                                    <td className="text-center">
                                        <a href={SHORT_LINK_REDIRECT_API + dataEntry.hash} className="text-black d-block text-danger" style={{ textDecoration: 'none' }}>{SHORT_LINK_REDIRECT_API + dataEntry.hash}</a>
                                    </td>
                                    <td className="text-center">{dataEntry.link}</td>
                                    <td className="d-flex justify-content-center btn btn-danger" onClick={() => this.deleteButtonClicked(dataEntry.hash)}>
                                        Delete Link
                                    </td>
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

export default ShortLinksComponent;
