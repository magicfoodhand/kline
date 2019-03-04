import React from 'react'
import gql from "graphql-tag";
import { Mutation } from "react-apollo";

const CREATE_USER = gql`
    mutation CreateUser($name: String!) {
        createUser(name: $name)
    }
`;

let NewUser = () => {
    let input = null
    return (<div className="App">
            <h3>New User</h3>
            <Mutation mutation={CREATE_USER}>
                {(createUser, { data }) => (
                    <div>
                        <form
                            onSubmit={e => {
                                e.preventDefault();
                                createUser({ variables: { name: input.value } });
                                input.value = "";
                                window.location = '/users'
                            }}
                        >
                            <label>
                                Name:
                                <input
                                    ref={node => {
                                        input = node;
                                    }}
                                />
                            </label>
                            <button type="submit">Add User</button>
                        </form>
                    </div>
                )}
            </Mutation>
        </div>
    )
}

export default NewUser