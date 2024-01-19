import React from 'react';
import './App.css';
import ControlPanel from './components/ControlPanel';
import Callendar from './components/Callendar';


function App() {

  const callendars = [
    {name: "Krzysztof Kowalski", appointments: [
      {
      id: 0,
      name: "Spotkanie z klientem",
      startTime: new Date(2024, 0, 16, 0, 0),
      endTime: new Date(2024, 0, 16, 12, 15),
      author: "Krzysztof Kowalski",
      participants: ["Jan Kowalski", "Anna Nowak", "Janusz Tracz"],
      desc: "blablabla",
      color: "#F9F7C9",
      type: 0
      },
      {
      id: 1,
      name: "Spotkanie z klientem",
      startTime: new Date(2024, 0, 18, 13, 15),
      endTime: new Date(2024, 0, 18, 14, 30),
      author: "Krzysztof Kowalski",
      participants: ["Jan Kowalski", "Anna Nowak", "Janusz Tracz"],
      desc: "blablabla",
      color: "#F9F7C9",
      type: 0
      },
    ]},
    {name: "Karol Warszawski", appointments: [
      {id: 0, name: "dentysta", startTime: new Date(2024, 0, 19, 12, 0), endTime: new Date(2024, 0, 19, 14, 30), author: "test", participants: ["vlskadfjlkaj", "sadfsd"], desc: "blablabla", color: "#F9F7C9", type: 0},
      {id: 1, name: "próba2aaaabbbbbbbba", startTime: new Date(2024, 0, 16, 16, 0), endTime: new Date(2024, 0, 16, 17, 30), author: "test", participants: ["vlskadfjlkaj", "sadfsd"], desc: "blablabla", color: "#FFCF81", type: 0},
      {id: 2, name: "próba3", startTime: new Date(2024, 0, 20, 13, 0), endTime: new Date(2024, 0, 20, 15, 30), author: "test", participants: ["vlskadfjlkaj", "sadfsd"], desc: "blablabla", color: "#FFCF81", type: 0},
    ]},
    {name: "Maria Stawowa", appointments: [
      {id: 0, name: "próba2aaaaaa", startTime: new Date(2024, 0, 16, 12, 15), endTime: new Date(2024, 0, 16, 14, 30), author: "test", participants: ["vlskadfjlkaj", "sadfsd"], desc: "blablabla", color: "#FFCF81", type: 0},
      {id: 1, name: "próba3", startTime: new Date(2024, 0, 21, 15, 0), endTime: new Date(2024, 0, 21, 19, 30), author: "test", participants: ["vlskadfjlkaj", "sadfsd"], desc: "blablabla", color: "#FFCF81", type: 0},
    ]},
    {name: "Magda Lena Magdalena", appointments: [
      {id: 0, name: "próba2aaaaaa", startTime: new Date(2024, 0, 21, 10, 30), endTime: new Date(2024, 0, 21, 12, 30), author: "test", participants: ["vlskadfjlkaj", "sadfsd"], desc: "blablabla", color: "#FFCF81", type: 0},
      {id: 1, name: "próba3", startTime: new Date(2024, 0, 19, 5, 10), endTime: new Date(2024, 0, 19, 6, 0), author: "test", participants: ["vlskadfjlkaj", "sadfsd"], desc: "blablabla", color: "#FFCF81", type: 0},
    ]},
  ]

  const [currentCallendarIdx, setCurrentCallendarIdx] = React.useState(0);
  const [currentAppointmentId, setCurrentAppointmentId] = React.useState(0);

  const currentCallendar = callendars[currentCallendarIdx];

  let currentAppointment = currentCallendar.appointments[0];
  currentCallendar.appointments.forEach(appointment => {
    if (appointment.id === currentAppointmentId) {
      currentAppointment = appointment;
    }
  });
  

  const callendarsList = []
  callendars.forEach(callendar => {
    callendarsList.push({id: callendars.indexOf(callendar), name: callendar.name})
  })

  return (
    <div className="app-wrapper">
        <ControlPanel callendarsList={callendarsList} currentAppointment={currentAppointment} setCallendar={setCurrentCallendarIdx}/>
        <Callendar appointments={currentCallendar.appointments} setCurrentAppointment={setCurrentAppointmentId} />
    </div>
  );
}

export default App;
