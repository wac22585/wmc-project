<template>
  <v-container>
    <div class="title">Users</div>
    <div class="subtitle">view and manage users</div>
    <div>
      <v-table>
        <thead>
          <tr>
            <th class="text-left table-head">User</th>
            <th class="table-head">Created</th>
            <th class="text-left table-head">Role</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="user in users"
            :key="user.id"
          >
            <td> <span class="username">{{ user.name }}</span> <br> <span>{{ user.email }}</span></td>
            <td>{{ user.created }}</td>
            <td>{{ user.role }}</td>
            <td><button><svg-icon type="mdi" :path="path"></svg-icon></button></td>
          </tr>
        </tbody>
      </v-table>
    </div>
  </v-container>
</template>

<script setup> 
  import axios from 'axios';

  import SvgIcon from '@jamescoyle/vue-icon';
  import { mdiDotsHorizontal } from '@mdi/js';
</script>

<script>
  export default {
    components: {
      SvgIcon
    },
      data() {
          return {
              users: [
                {id: 1, name: "Emil Wache", email: "eow@gmx.at", created: "10.10.2023", role: "Gast"},
                {id: 2, name: "Admin", email: "admin@spengergasse.at", created: "07.12.2023", role: "Administrator"},
                {id: 3, name: "Test", email: "test@gmail.com", created: "14.11.2023", role: "Administrator"},
              ],
              path: mdiDotsHorizontal,
          };
      },
      async mounted() {
          try {
              //const response = await axios.get("users/all");
              //this.users = response.date;
          }
          catch(e)
          {
              alert(e)
          }
      },
      methods: {

      },
  }
</script>

<style scoped>
  .title {
    color: black;
    font-size: 30px;
  }

  .subtitle {
    color: black;
    font-size: 20px;
    margin-top: -10px;
  }

  .table-head {
    color: #707070;
  }

  .username {
    font-weight: bold;
  }
</style>