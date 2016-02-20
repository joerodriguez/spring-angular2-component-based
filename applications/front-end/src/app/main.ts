import {bootstrap}    from 'angular2/platform/browser'
import {AppComponent} from "./app.component";
import {SessionService} from "./session.service";
import {HTTP_PROVIDERS} from "angular2/http";

bootstrap(AppComponent, [SessionService, HTTP_PROVIDERS]);
