import {Component} from 'angular2/core';
import {SessionService} from "./session.service";

@Component({
    selector: 'app',

    template: `
        <h1>Login</h1>

        <input #email>
        <input #password type="password">
        <input type="submit" (click)="login(email.value, password.value)">

        {{message}}
    `
})
export class AppComponent {
    message: string = "";

    constructor(private _sessionService:SessionService) {}

    login(email: string, password: string) {
        this._sessionService.create(email, password).subscribe(
            success => this.message = "Login succeeded",
            error => this.message = "Login failed"
        );
    }
}