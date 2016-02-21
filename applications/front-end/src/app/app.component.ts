import {Component} from 'angular2/core';
import {Router, RouteConfig, ROUTER_DIRECTIVES} from "angular2/router";

import {LoginComponent} from "./sessions/login.component";
import {UsersDashboardComponent} from "./users/users.dashboard.component";
import {SessionService} from "./sessions/session.service";

@Component({
    selector: 'app',

    template: `
        <nav [hidden]="!loggedIn">
          <a [routerLink]="['Users']">Users</a>
          <a (click)="logout()">Logout</a>
        </nav>

        <router-outlet></router-outlet>
    `,
    directives: [ROUTER_DIRECTIVES]
})

@RouteConfig([
    {path: '/login', name: 'Login', component: LoginComponent, useAsDefault: true},
    {path: '/users', name: 'Users', component: UsersDashboardComponent}
])

export class AppComponent {
    loggedIn: boolean = false;


    constructor(
        private _router: Router,
        private _sessionService: SessionService
    ) {
        _sessionService.loggedInStatusStream().subscribe(
            (loggedInStatus) => { this.loggedIn = loggedInStatus; }
        )
    }

    logout() {
        this._sessionService.destroy().subscribe(
            () => {
                this._router.navigate(['Login']);
            }
        )
    }

}