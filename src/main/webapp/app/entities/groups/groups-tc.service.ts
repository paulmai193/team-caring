import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { GroupsTc } from './groups-tc.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class GroupsTcService {

    private resourceUrl = SERVER_API_URL + 'api/groups';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/groups';

    constructor(private http: Http) { }

    create(groups: GroupsTc): Observable<GroupsTc> {
        const copy = this.convert(groups);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(groups: GroupsTc): Observable<GroupsTc> {
        const copy = this.convert(groups);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<GroupsTc> {
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
     * Convert a returned JSON object to GroupsTc.
     */
    private convertItemFromServer(json: any): GroupsTc {
        const entity: GroupsTc = Object.assign(new GroupsTc(), json);
        return entity;
    }

    /**
     * Convert a GroupsTc to a JSON which can be sent to the server.
     */
    private convert(groups: GroupsTc): GroupsTc {
        const copy: GroupsTc = Object.assign({}, groups);
        return copy;
    }
}
