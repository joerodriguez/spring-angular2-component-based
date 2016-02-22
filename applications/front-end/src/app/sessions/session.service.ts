import {Http, Response, Headers, HTTP_PROVIDERS} from 'angular2/http';
import {EventEmitter, Injectable} from "angular2/core";
import {Observable} from "rxjs/Observable";
import {Subject} from "rxjs/Subject";
import 'rxjs/add/operator/do'

@Injectable()
export class SessionService {

    private _loginStatus:EventEmitter<boolean> = new EventEmitter<boolean>();
    get loginStatus(): EventEmitter<boolean> {
        return this._loginStatus
    }

    constructor(private _http:Http) {
    }

    create(email:string, password:string):Observable<Response> {
        let body = JSON.stringify({email: email, password: password});
        let headers = new Headers({'Content-Type': 'application/json'});

        return this._http.post("/api/sessions", body, {headers}).do(
            () => this._loginStatus.emit(true)
        );
    }

    destroy():Observable<Response> {
        return this._http.delete("/api/sessions").do(
            () => this._loginStatus.emit(false)
        );
    }
}
