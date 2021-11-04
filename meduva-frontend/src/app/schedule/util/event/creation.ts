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

export function createOffWorkHoursEvent(start: Date, end: Date): CalendarEvent {

  return {
    draggable: false,
    end: new Date(end),
    id: undefined,
    meta: undefined,
    start: new Date(start),
    title: "Off work",
    color: {
      primary: "lightGray",
      secondary: "lightGray"
    }
  };
}

export function createAbsenceHoursEvent(start: Date, end: Date): CalendarEvent {

  return {
    draggable: false,
    end: new Date(end),
    id: undefined,
    meta: undefined,
    start: new Date(start),
    title: "Absence",
    color: {
      primary: "#FF9191",
      secondary: "#FF9191"
    }
  };
}
