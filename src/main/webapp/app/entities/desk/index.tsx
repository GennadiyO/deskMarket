import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Desk from './desk';
import DeskDetail from './desk-detail';
import DeskUpdate from './desk-update';
import DeskDeleteDialog from './desk-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DeskDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DeskUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DeskUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DeskDetail} />
      <ErrorBoundaryRoute path={match.url} component={Desk} />
    </Switch>
  </>
);

export default Routes;
