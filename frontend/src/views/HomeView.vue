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
            <td> <span class="username">{{ user.firstname }} {{ user.lastname }}</span> <br> <span>{{ user.email }}</span></td>
            <td>{{ new Date(user.created).toLocaleDateString("de-DE") }}</td>
            <td>{{ user.roles }}</td>
            <td><button class="btn-more"><svg-icon type="mdi" :path="path"></svg-icon></button></td>
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
              users: [],
              path: mdiDotsHorizontal,
          };
      },
      async mounted() {
          try {
              const response = await axios.get("users/all");
              console.log(response);
              this.users = response.data;
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

  .btn-more {
    border-radius: 50%;
    padding: 2px 4px;
  }
</style>