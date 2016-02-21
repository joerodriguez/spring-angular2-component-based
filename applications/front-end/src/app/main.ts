import {bootstrap}    from 'angular2/platform/browser'

import {HTTP_PROVIDERS} from "angular2/http";
import {ROUTER_PROVIDERS} from "angular2/router";

import 'rxjs/Rx'

import {AppComponent} from "./app.component";
import {SessionService} from "./sessions/session.service";

bootstrap(AppComponent, [SessionService, HTTP_PROVIDERS, ROUTER_PROVIDERS]);
