import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './desk.reducer';
import { IDesk } from 'app/shared/model/desk.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDeskDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const DeskDetail = (props: IDeskDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { deskEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Desk [<b>{deskEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="deskId">Desk Id</span>
          </dt>
          <dd>{deskEntity.deskId}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{deskEntity.type}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{deskEntity.name}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{deskEntity.description}</dd>
          <dt>
            <span id="creationDate">Creation Date</span>
          </dt>
          <dd>
            <TextFormat value={deskEntity.creationDate} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="modificationDate">Modification Date</span>
          </dt>
          <dd>
            <TextFormat value={deskEntity.modificationDate} type="date" format={APP_DATE_FORMAT} />
          </dd>
        </dl>
        <Button tag={Link} to="/desk" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/desk/${deskEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ desk }: IRootState) => ({
  deskEntity: desk.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DeskDetail);
