export function trimJSON(jsonObject: any, properties: string[]): any {
  properties.forEach(propName => {
    delete jsonObject[propName];
  });
  return jsonObject;
}
