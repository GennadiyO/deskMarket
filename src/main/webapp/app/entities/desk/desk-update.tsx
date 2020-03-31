import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './desk.reducer';
import { IDesk } from 'app/shared/model/desk.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IDeskUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const DeskUpdate = (props: IDeskUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { deskEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/desk');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.creationDate = convertDateTimeToServer(values.creationDate);
    values.modificationDate = convertDateTimeToServer(values.modificationDate);

    if (errors.length === 0) {
      const entity = {
        ...deskEntity,
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
          <h2 id="deskMarketApp.desk.home.createOrEditLabel">Create or edit a Desk</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : deskEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="desk-id">ID</Label>
                  <AvInput id="desk-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="deskIdLabel" for="desk-deskId">
                  Desk Id
                </Label>
                <AvField
                  id="desk-deskId"
                  type="string"
                  className="form-control"
                  name="deskId"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="typeLabel" for="desk-type">
                  Type
                </Label>
                <AvInput
                  id="desk-type"
                  type="select"
                  className="form-control"
                  name="type"
                  value={(!isNew && deskEntity.type) || 'LIFTING_DESK_OVERLAY'}
                >
                  <option value="LIFTING_DESK_OVERLAY">LIFTING_DESK_OVERLAY</option>
                  <option value="LIFTING_DESK">LIFTING_DESK</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="nameLabel" for="desk-name">
                  Name
                </Label>
                <AvField
                  id="desk-name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="descriptionLabel" for="desk-description">
                  Description
                </Label>
                <AvField id="desk-description" type="text" name="description" />
              </AvGroup>
              <AvGroup>
                <Label id="creationDateLabel" for="desk-creationDate">
                  Creation Date
                </Label>
                <AvInput
                  id="desk-creationDate"
                  type="datetime-local"
                  className="form-control"
                  name="creationDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.deskEntity.creationDate)}
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="modificationDateLabel" for="desk-modificationDate">
                  Modification Date
                </Label>
                <AvInput
                  id="desk-modificationDate"
                  type="datetime-local"
                  className="form-control"
                  name="modificationDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.deskEntity.modificationDate)}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/desk" replace color="info">
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
  deskEntity: storeState.desk.entity,
  loading: storeState.desk.loading,
  updating: storeState.desk.updating,
  updateSuccess: storeState.desk.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DeskUpdate);
