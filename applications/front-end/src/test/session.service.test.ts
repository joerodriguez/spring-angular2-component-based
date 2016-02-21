import {injectAsync, it, describe, beforeEach, beforeEachProviders, inject} from 'angular2/testing';
import {MockBackend,MockConnection} from "angular2/src/http/backends/mock_backend";
import {Http, XHRBackend, HTTP_PROVIDERS, ResponseOptions, Response} from "angular2/http";
import {provide} from "angular2/core";
import {SessionService} from "../app/sessions/session.service";
import {RequestMethod} from "angular2/http";


describe('SessionService', () => {

    beforeEachProviders(() => {
        return [
            HTTP_PROVIDERS,
            provide(XHRBackend, {useClass: MockBackend}),
            SessionService
        ];
    });

    it('logs in', injectAsync([XHRBackend, SessionService], (mockBackend, sessionService) => {
        return new Promise(
            (resolve) => {

                mockBackend.connections.subscribe(connection => {

                    var data = JSON.parse(connection.request.text());
                    expect(data.email).toEqual("tester@example.com");
                    expect(data.password).toEqual("test-password");

                    expect(connection.request.url.toString()).toEqual("/api/sessions");
                    expect(connection.request.method).toEqual(RequestMethod.Post);

                    connection.mockRespond(new ResponseOptions({status: 200}));
                });

                sessionService.create("tester@example.com", "test-password").subscribe(
                    (res) => {
                        expect(res.status).toBe(200);
                        resolve();
                    }
                )
            }
        );
    }));

});
