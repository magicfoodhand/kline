import React, { Component } from 'react';
import './App.css';
import ApolloClient from 'apollo-boost';
import {ApolloProvider} from "react-apollo";
import {
    BrowserRouter as Router, Switch, Route, Link
} from 'react-router-dom'

const client = new ApolloClient({
    uri: '/api/graphql'
});

let Home = () => (
    <div className="App">
      <nav>
        <ul>
          <li><Link to="/">Home</Link></li>
        </ul>
      </nav>
    </div>
)

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
              <Route exact path="/" component={Home}/>
          </ApolloProvider>
      </Router>
    );
  }
}

export default App;
