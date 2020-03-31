import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DeskProperty from './desk-property';
import DeskPropertyDetail from './desk-property-detail';
import DeskPropertyUpdate from './desk-property-update';
import DeskPropertyDeleteDialog from './desk-property-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DeskPropertyDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DeskPropertyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DeskPropertyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DeskPropertyDetail} />
      <ErrorBoundaryRoute path={match.url} component={DeskProperty} />
    </Switch>
  </>
);

export default Routes;
