import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { AttendeeTc } from './attendee-tc.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class AttendeeTcService {

    private resourceUrl = SERVER_API_URL + 'api/attendees';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/attendees';

    constructor(private http: Http) { }

    create(attendee: AttendeeTc): Observable<AttendeeTc> {
        const copy = this.convert(attendee);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(attendee: AttendeeTc): Observable<AttendeeTc> {
        const copy = this.convert(attendee);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<AttendeeTc> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to AttendeeTc.
     */
    private convertItemFromServer(json: any): AttendeeTc {
        const entity: AttendeeTc = Object.assign(new AttendeeTc(), json);
        return entity;
    }

    /**
     * Convert a AttendeeTc to a JSON which can be sent to the server.
     */
    private convert(attendee: AttendeeTc): AttendeeTc {
        const copy: AttendeeTc = Object.assign({}, attendee);
        return copy;
    }
}
