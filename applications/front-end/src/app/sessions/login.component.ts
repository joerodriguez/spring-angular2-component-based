import {Component} from 'angular2/core';
import {SessionService} from "./session.service";

@Component({
    selector: 'login',

    template: `
        <form id="login">
            <h1>Login</h1>

            <input #email id="email">
            <input #password type="password" id="password">
            <input type="submit" (click)="login(email.value, password.value)">
        {{message}}
        </form>
    `
})

export class LoginComponent {
    message:string = "";

    constructor(private _sessionService:SessionService) {
    }

    login(email:string, password:string) {
        this._sessionService.create(email, password).subscribe(
            success => this.message = "Login succeeded",
            error => this.message = "Login failed"
        );
    }
}
