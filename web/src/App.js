import React, { Component } from 'react';
import './App.css';
import ApolloClient from 'apollo-boost';
import {ApolloProvider} from "react-apollo";
import {
    BrowserRouter as Router, Switch, Route, Link
} from 'react-router-dom'

import Home from './Home/Home'
import Users from './Users/Users'
import NewUser from "./NewUser/NewUser";

const client = new ApolloClient({
    uri: 'https://lit-citadel-61221.herokuapp.com/graphql'
});

let NotFound = () => (
    <div>
      <h1>Sorry this isn't what you're looking for.</h1>
    </div>
)

class App extends Component {
  render() {
    return (
      <Router>
          <ApolloProvider client={client}>
              <nav>
                  <ul className='menu'>
                      <li><Link to="/">Home</Link></li>
                      <li><Link to="/users">Users</Link></li>
                  </ul>
              </nav>
              <Switch>
                  <Route exact path="/" component={Home}/>
                  <Route exact path="/users" component={Users}/>
                  <Route exact path="/users/new" component={NewUser}/>
                  <Route path="/" component={NotFound}/>
              </Switch>
          </ApolloProvider>
      </Router>
    );
  }
}

export default App;
