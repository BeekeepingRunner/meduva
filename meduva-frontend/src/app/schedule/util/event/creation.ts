import {CalendarEvent} from "angular-calendar";

export function createUnavailabilityEvent(start: Date, end: Date): CalendarEvent {

  return {
    draggable: false,
    end: new Date(end),
    id: undefined,
    meta: undefined,
    start: new Date(start),
    title: "Unavailable",
    color: {
      primary: "#FF9191",
      secondary: "#FF9191"
    }
  };
}
