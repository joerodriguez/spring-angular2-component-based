import {Http, Response, Headers, HTTP_PROVIDERS} from 'angular2/http';
import {Injectable} from "angular2/core";
import {Observable} from "rxjs/Observable";
import {Subject} from "rxjs/Subject";
import 'rxjs/add/operator/map'

@Injectable()
export class SessionService {

    _loginStatus:Subject<boolean> = new Subject<boolean>();

    constructor(private _http:Http) {
    }

    create(email:string, password:string):Observable<Response> {
        let body = JSON.stringify({email: email, password: password});
        let headers = new Headers({'Content-Type': 'application/json'});

        return this._http.post("/api/sessions", body, {headers}).map(
            <Response>(response) => {
                this._loginStatus.next(true);
                return response;
            }
        );
    }

    destroy():Observable<Response> {
        return this._http.delete("/api/sessions").map(
            <Response>(response) => {
                this._loginStatus.next(false);
                return response;
            }
        );
    }

    loggedInStatusStream():Observable<boolean> {
        return this._loginStatus;
    }
}
