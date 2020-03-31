import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Desk from './desk';
import Photo from './photo';
import Price from './price';
import DeskProperty from './desk-property';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}desk`} component={Desk} />
      <ErrorBoundaryRoute path={`${match.url}photo`} component={Photo} />
      <ErrorBoundaryRoute path={`${match.url}price`} component={Price} />
      <ErrorBoundaryRoute path={`${match.url}desk-property`} component={DeskProperty} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
