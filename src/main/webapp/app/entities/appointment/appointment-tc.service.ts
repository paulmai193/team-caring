import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { AppointmentTc } from './appointment-tc.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class AppointmentTcService {

    private resourceUrl = SERVER_API_URL + 'api/appointments';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/appointments';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(appointment: AppointmentTc): Observable<AppointmentTc> {
        const copy = this.convert(appointment);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(appointment: AppointmentTc): Observable<AppointmentTc> {
        const copy = this.convert(appointment);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<AppointmentTc> {
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
     * Convert a returned JSON object to AppointmentTc.
     */
    private convertItemFromServer(json: any): AppointmentTc {
        const entity: AppointmentTc = Object.assign(new AppointmentTc(), json);
        entity.time = this.dateUtils
            .convertDateTimeFromServer(json.time);
        return entity;
    }

    /**
     * Convert a AppointmentTc to a JSON which can be sent to the server.
     */
    private convert(appointment: AppointmentTc): AppointmentTc {
        const copy: AppointmentTc = Object.assign({}, appointment);

        copy.time = this.dateUtils.toDate(appointment.time);
        return copy;
    }
}
