import {Http, Response, Headers, HTTP_PROVIDERS} from 'angular2/http';
import {Injectable} from "angular2/core";

@Injectable()
export class SessionService {
    constructor(private _http:Http) {}

    create(email:string, password:string) {
        let body = JSON.stringify({email: email, password: password});
        let headers = new Headers({'Content-Type': 'application/json'});

        return this._http.post("/api/sessions", body, {headers});
    }

    delete() {
        return this._http.delete("/api/sessions");
    }
}
