import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './desk-property.reducer';
import { IDeskProperty } from 'app/shared/model/desk-property.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDeskPropertyDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const DeskPropertyDetail = (props: IDeskPropertyDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { deskPropertyEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          DeskProperty [<b>{deskPropertyEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="deskPropertyId">Desk Property Id</span>
          </dt>
          <dd>{deskPropertyEntity.deskPropertyId}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{deskPropertyEntity.type}</dd>
          <dt>
            <span id="value">Value</span>
          </dt>
          <dd>{deskPropertyEntity.value}</dd>
          <dt>Desk</dt>
          <dd>{deskPropertyEntity.deskDeskId ? deskPropertyEntity.deskDeskId : ''}</dd>
        </dl>
        <Button tag={Link} to="/desk-property" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/desk-property/${deskPropertyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ deskProperty }: IRootState) => ({
  deskPropertyEntity: deskProperty.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DeskPropertyDetail);
