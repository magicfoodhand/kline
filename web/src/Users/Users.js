import React from 'react'
import gql from "graphql-tag";
import { Query } from "react-apollo";
import {Link} from "react-router-dom";

const GET_USERS = gql`
  {
    users {
      all {
        name
      }
    }
  }
`;

let Users = () => (
    <div className="App">
        <h3>Users</h3>
        <Link to="/users/new">New User</Link>
        <Query query={GET_USERS}>
            {({ loading, error, data }) => {
                if (loading) return "Loading...";
                if (error) return `Error! ${error.message}`;

                return (
                    <ul name="users">
                        {data.users.all.map(({name}, index) => (
                            <li key={index}>
                                {name}
                            </li>
                        ))}
                    </ul>
                );
            }}
        </Query>
    </div>
)

export default Users
