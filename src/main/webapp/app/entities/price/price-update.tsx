import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IDesk } from 'app/shared/model/desk.model';
import { getEntities as getDesks } from 'app/entities/desk/desk.reducer';
import { getEntity, updateEntity, createEntity, reset } from './price.reducer';
import { IPrice } from 'app/shared/model/price.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPriceUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PriceUpdate = (props: IPriceUpdateProps) => {
  const [deskId, setDeskId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { priceEntity, desks, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/price');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getDesks();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.creationDate = convertDateTimeToServer(values.creationDate);

    if (errors.length === 0) {
      const entity = {
        ...priceEntity,
        ...values
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="deskMarketApp.price.home.createOrEditLabel">Create or edit a Price</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : priceEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="price-id">ID</Label>
                  <AvInput id="price-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="priceIdLabel" for="price-priceId">
                  Price Id
                </Label>
                <AvField
                  id="price-priceId"
                  type="string"
                  className="form-control"
                  name="priceId"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="creationDateLabel" for="price-creationDate">
                  Creation Date
                </Label>
                <AvInput
                  id="price-creationDate"
                  type="datetime-local"
                  className="form-control"
                  name="creationDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.priceEntity.creationDate)}
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="priceLabel" for="price-price">
                  Price
                </Label>
                <AvField
                  id="price-price"
                  type="text"
                  name="price"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="price-desk">Desk</Label>
                <AvInput id="price-desk" type="select" className="form-control" name="deskId" required>
                  {desks
                    ? desks.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.deskId}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>This field is required.</AvFeedback>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/price" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  desks: storeState.desk.entities,
  priceEntity: storeState.price.entity,
  loading: storeState.price.loading,
  updating: storeState.price.updating,
  updateSuccess: storeState.price.updateSuccess
});

const mapDispatchToProps = {
  getDesks,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PriceUpdate);
