export enum Visibility {
  PUBLIC = 'PUBLIC',
  FRIENDS_ONLY = 'FRIENDS_ONLY',
  PRIVATE = 'PRIVATE',
}

export interface ModpackModEntry {
  modVersionId: number;
  modId: number;
  modName: string;
  modExternalId: string;
  iconUrl?: string;
  version: string;
  minecraftVersion: string;
  loader: string;
  required: boolean;
}

export interface Modpack {
  id: number;
  name: string;
  description: string;
  minecraftVersion: string;
  loader: string;
  visibility: Visibility;
  ownerUsername: string;
  mods?: ModpackModEntry[];
}

export interface ModpackRequest {
  name: string;
  description: string;
  minecraftVersion: string;
  loader: string;
  visibility: Visibility;
}

export interface AddModToModpackRequest {
  modVersionId: number;
  required: boolean;
}
