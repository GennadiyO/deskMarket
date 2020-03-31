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

import { IDesk, defaultValue } from 'app/shared/model/desk.model';

export const ACTION_TYPES = {
  FETCH_DESK_LIST: 'desk/FETCH_DESK_LIST',
  FETCH_DESK: 'desk/FETCH_DESK',
  CREATE_DESK: 'desk/CREATE_DESK',
  UPDATE_DESK: 'desk/UPDATE_DESK',
  DELETE_DESK: 'desk/DELETE_DESK',
  RESET: 'desk/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDesk>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type DeskState = Readonly<typeof initialState>;

// Reducer

export default (state: DeskState = initialState, action): DeskState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_DESK_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DESK):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_DESK):
    case REQUEST(ACTION_TYPES.UPDATE_DESK):
    case REQUEST(ACTION_TYPES.DELETE_DESK):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_DESK_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DESK):
    case FAILURE(ACTION_TYPES.CREATE_DESK):
    case FAILURE(ACTION_TYPES.UPDATE_DESK):
    case FAILURE(ACTION_TYPES.DELETE_DESK):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_DESK_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_DESK):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_DESK):
    case SUCCESS(ACTION_TYPES.UPDATE_DESK):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_DESK):
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

const apiUrl = 'api/desks';

// Actions

export const getEntities: ICrudGetAllAction<IDesk> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_DESK_LIST,
    payload: axios.get<IDesk>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IDesk> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DESK,
    payload: axios.get<IDesk>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IDesk> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DESK,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IDesk> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DESK,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDesk> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DESK,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
