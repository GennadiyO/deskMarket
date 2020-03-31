import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './price.reducer';
import { IPrice } from 'app/shared/model/price.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPriceDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PriceDetail = (props: IPriceDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { priceEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Price [<b>{priceEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="priceId">Price Id</span>
          </dt>
          <dd>{priceEntity.priceId}</dd>
          <dt>
            <span id="creationDate">Creation Date</span>
          </dt>
          <dd>
            <TextFormat value={priceEntity.creationDate} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{priceEntity.price}</dd>
          <dt>Desk</dt>
          <dd>{priceEntity.deskDeskId ? priceEntity.deskDeskId : ''}</dd>
        </dl>
        <Button tag={Link} to="/price" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/price/${priceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ price }: IRootState) => ({
  priceEntity: price.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PriceDetail);
