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
import { getEntity, updateEntity, createEntity, reset } from './desk-property.reducer';
import { IDeskProperty } from 'app/shared/model/desk-property.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IDeskPropertyUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const DeskPropertyUpdate = (props: IDeskPropertyUpdateProps) => {
  const [deskId, setDeskId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { deskPropertyEntity, desks, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/desk-property');
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
    if (errors.length === 0) {
      const entity = {
        ...deskPropertyEntity,
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
          <h2 id="deskMarketApp.deskProperty.home.createOrEditLabel">Create or edit a DeskProperty</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : deskPropertyEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="desk-property-id">ID</Label>
                  <AvInput id="desk-property-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="deskPropertyIdLabel" for="desk-property-deskPropertyId">
                  Desk Property Id
                </Label>
                <AvField
                  id="desk-property-deskPropertyId"
                  type="string"
                  className="form-control"
                  name="deskPropertyId"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="typeLabel" for="desk-property-type">
                  Type
                </Label>
                <AvInput
                  id="desk-property-type"
                  type="select"
                  className="form-control"
                  name="type"
                  value={(!isNew && deskPropertyEntity.type) || 'LENGTH'}
                >
                  <option value="LENGTH">LENGTH</option>
                  <option value="WIDTH">WIDTH</option>
                  <option value="HEIGHT">HEIGHT</option>
                  <option value="MIN_HEIGHT">MIN_HEIGHT</option>
                  <option value="MAX_HEIGHT">MAX_HEIGHT</option>
                  <option value="LIFTING_CAPACITY">LIFTING_CAPACITY</option>
                  <option value="WEIGHT">WEIGHT</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="valueLabel" for="desk-property-value">
                  Value
                </Label>
                <AvField
                  id="desk-property-value"
                  type="text"
                  name="value"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="desk-property-desk">Desk</Label>
                <AvInput id="desk-property-desk" type="select" className="form-control" name="deskId" required>
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
              <Button tag={Link} id="cancel-save" to="/desk-property" replace color="info">
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
  deskPropertyEntity: storeState.deskProperty.entity,
  loading: storeState.deskProperty.loading,
  updating: storeState.deskProperty.updating,
  updateSuccess: storeState.deskProperty.updateSuccess
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

export default connect(mapStateToProps, mapDispatchToProps)(DeskPropertyUpdate);
