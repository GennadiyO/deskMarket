import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IDeskProperty, defaultValue } from 'app/shared/model/desk-property.model';

export const ACTION_TYPES = {
  FETCH_DESKPROPERTY_LIST: 'deskProperty/FETCH_DESKPROPERTY_LIST',
  FETCH_DESKPROPERTY: 'deskProperty/FETCH_DESKPROPERTY',
  CREATE_DESKPROPERTY: 'deskProperty/CREATE_DESKPROPERTY',
  UPDATE_DESKPROPERTY: 'deskProperty/UPDATE_DESKPROPERTY',
  DELETE_DESKPROPERTY: 'deskProperty/DELETE_DESKPROPERTY',
  RESET: 'deskProperty/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDeskProperty>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type DeskPropertyState = Readonly<typeof initialState>;

// Reducer

export default (state: DeskPropertyState = initialState, action): DeskPropertyState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_DESKPROPERTY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DESKPROPERTY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_DESKPROPERTY):
    case REQUEST(ACTION_TYPES.UPDATE_DESKPROPERTY):
    case REQUEST(ACTION_TYPES.DELETE_DESKPROPERTY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_DESKPROPERTY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DESKPROPERTY):
    case FAILURE(ACTION_TYPES.CREATE_DESKPROPERTY):
    case FAILURE(ACTION_TYPES.UPDATE_DESKPROPERTY):
    case FAILURE(ACTION_TYPES.DELETE_DESKPROPERTY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_DESKPROPERTY_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_DESKPROPERTY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_DESKPROPERTY):
    case SUCCESS(ACTION_TYPES.UPDATE_DESKPROPERTY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_DESKPROPERTY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/desk-properties';

// Actions

export const getEntities: ICrudGetAllAction<IDeskProperty> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_DESKPROPERTY_LIST,
    payload: axios.get<IDeskProperty>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IDeskProperty> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DESKPROPERTY,
    payload: axios.get<IDeskProperty>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IDeskProperty> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DESKPROPERTY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IDeskProperty> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DESKPROPERTY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDeskProperty> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DESKPROPERTY,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
