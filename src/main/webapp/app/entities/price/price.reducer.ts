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

import { IPrice, defaultValue } from 'app/shared/model/price.model';

export const ACTION_TYPES = {
  FETCH_PRICE_LIST: 'price/FETCH_PRICE_LIST',
  FETCH_PRICE: 'price/FETCH_PRICE',
  CREATE_PRICE: 'price/CREATE_PRICE',
  UPDATE_PRICE: 'price/UPDATE_PRICE',
  DELETE_PRICE: 'price/DELETE_PRICE',
  RESET: 'price/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPrice>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type PriceState = Readonly<typeof initialState>;

// Reducer

export default (state: PriceState = initialState, action): PriceState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PRICE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PRICE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PRICE):
    case REQUEST(ACTION_TYPES.UPDATE_PRICE):
    case REQUEST(ACTION_TYPES.DELETE_PRICE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PRICE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PRICE):
    case FAILURE(ACTION_TYPES.CREATE_PRICE):
    case FAILURE(ACTION_TYPES.UPDATE_PRICE):
    case FAILURE(ACTION_TYPES.DELETE_PRICE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRICE_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_PRICE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PRICE):
    case SUCCESS(ACTION_TYPES.UPDATE_PRICE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PRICE):
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

const apiUrl = 'api/prices';

// Actions

export const getEntities: ICrudGetAllAction<IPrice> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PRICE_LIST,
    payload: axios.get<IPrice>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IPrice> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PRICE,
    payload: axios.get<IPrice>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPrice> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PRICE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IPrice> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PRICE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPrice> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PRICE,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
