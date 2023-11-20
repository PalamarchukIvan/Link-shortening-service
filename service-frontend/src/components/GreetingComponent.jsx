import React, {Component} from 'react';
import {Link} from "react-router-dom";

class GreetingComponent extends Component {
    constructor(props) {
        super(props);

        this.gotToMainPage = this.gotToMainPage.bind(this)
    }
    gotToMainPage() {
        console.log("clicked")
    }
    render() {
        return (
            <div>
                <div class="jumbotron">
                    <h1 class="display-4">Сокращение ссылок</h1>
                    <p class="lead">Сделайте ваши ссылки короче и удобнее с нашим сервисом.</p>
                    <hr class="my-4"></hr>
                        <p>Просто войдите или зарегистрируйтесь, чтобы начать использовать сервис!</p>
                        <p class="lead">
                            <a className="btn btn-primary btn-lg" onClick={this.gotToMainPage} href="/main">Войти</a>
                            <a class="btn btn-secondary btn-lg" href="#" role="button">Зарегистрироваться</a>
                        </p>
                </div>

                <div class="container text-center">
                    <h2 class="mb-4">Наши преимущества</h2>
                    <div class="row">
                        <div class="col-md-4 mb-4">
                            <div class="animated fadeInDown delay-1s">
                                <i class="fas fa-link fa-3x mb-2"></i>
                                <h4>Короткие ссылки</h4>
                                <p>Сделайте ваши ссылки лаконичными и удобными для обмена.</p>
                            </div>
                        </div>
                        <div class="col-md-4 mb-4">
                            <div class="animated fadeInDown delay-2s">
                                <i class="fas fa-chart-line fa-3x mb-2"></i>
                                <h4>Аналитика</h4>
                                <p>Отслеживайте статистику переходов по вашим сокращенным ссылкам.</p>
                            </div>
                        </div>
                        <div class="col-md-4 mb-4">
                            <div class="animated fadeInDown delay-3s">
                                <i class="fas fa-lock fa-3x mb-2"></i>
                                <h4>Безопасность</h4>
                                <p>Ваши данные защищены и сохранены в безопасности.</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default GreetingComponent;