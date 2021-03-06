import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './photo.reducer';
import { IPhoto } from 'app/shared/model/photo.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPhotoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PhotoDetail = (props: IPhotoDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { photoEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Photo [<b>{photoEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="photoId">Photo Id</span>
          </dt>
          <dd>{photoEntity.photoId}</dd>
          <dt>
            <span id="photo">Photo</span>
          </dt>
          <dd>
            {photoEntity.photo ? (
              <div>
                <a onClick={openFile(photoEntity.photoContentType, photoEntity.photo)}>
                  <img src={`data:${photoEntity.photoContentType};base64,${photoEntity.photo}`} style={{ maxHeight: '30px' }} />
                </a>
                <span>
                  {photoEntity.photoContentType}, {byteSize(photoEntity.photo)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>Desk</dt>
          <dd>{photoEntity.deskDeskId ? photoEntity.deskDeskId : ''}</dd>
        </dl>
        <Button tag={Link} to="/photo" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/photo/${photoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ photo }: IRootState) => ({
  photoEntity: photo.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PhotoDetail);
