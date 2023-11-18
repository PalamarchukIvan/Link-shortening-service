import React, { Component } from 'react'

class HeaderComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {

        }
    }

    render() {
        return (
            <div>
                <header>
                    <nav className="navbar navbar-expand-md navbar-dark bg-dark">
                         <h3 className="text-white  ml-5">Link-shortening service</h3>
                    </nav>
                </header>
            </div>
        )
    }
}

export default HeaderComponent