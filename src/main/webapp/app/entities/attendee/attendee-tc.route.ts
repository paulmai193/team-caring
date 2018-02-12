import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { AttendeeTcComponent } from './attendee-tc.component';
import { AttendeeTcDetailComponent } from './attendee-tc-detail.component';
import { AttendeeTcPopupComponent } from './attendee-tc-dialog.component';
import { AttendeeTcDeletePopupComponent } from './attendee-tc-delete-dialog.component';

@Injectable()
export class AttendeeTcResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const attendeeRoute: Routes = [
    {
        path: 'attendee-tc',
        component: AttendeeTcComponent,
        resolve: {
            'pagingParams': AttendeeTcResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Attendees'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'attendee-tc/:id',
        component: AttendeeTcDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Attendees'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const attendeePopupRoute: Routes = [
    {
        path: 'attendee-tc-new',
        component: AttendeeTcPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Attendees'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'attendee-tc/:id/edit',
        component: AttendeeTcPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Attendees'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'attendee-tc/:id/delete',
        component: AttendeeTcDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Attendees'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
