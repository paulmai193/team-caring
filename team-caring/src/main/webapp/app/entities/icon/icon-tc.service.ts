import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { IconTc } from './icon-tc.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class IconTcService {

    private resourceUrl = SERVER_API_URL + 'api/icons';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/icons';

    constructor(private http: Http) { }

    create(icon: IconTc): Observable<IconTc> {
        const copy = this.convert(icon);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(icon: IconTc): Observable<IconTc> {
        const copy = this.convert(icon);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<IconTc> {
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
     * Convert a returned JSON object to IconTc.
     */
    private convertItemFromServer(json: any): IconTc {
        const entity: IconTc = Object.assign(new IconTc(), json);
        return entity;
    }

    /**
     * Convert a IconTc to a JSON which can be sent to the server.
     */
    private convert(icon: IconTc): IconTc {
        const copy: IconTc = Object.assign({}, icon);
        return copy;
    }
}
