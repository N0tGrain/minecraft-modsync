/**
 * Represents a Minecraft mod with its metadata from the backend.
 */
export interface Mod {
  id: number;
  externalId: string;
  name: string;
  slug: string;
  description: string;
  iconUrl: string;
  downloads: number;
  categories: string[];
  versions?: ModVersion[];
}

/**
 * Represents a version of a Minecraft mod.
 */
export interface ModVersion {
  id: number;
  modId: number;
  externalVersionId: string;
  versionNumber: string;
  minecraftVersions?: string[];
  releaseType?: string;
}
