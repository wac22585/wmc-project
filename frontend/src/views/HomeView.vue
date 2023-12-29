<template>
  <v-container>
    <div class="title">Users</div>
    <div class="subtitle">view and manage users</div>
    <div class="add-btns">
      <InputField width="700px" label="Search" class="inputfield"></InputField>
      <v-divider vertical class="mx-2"></v-divider>
      <Button content="Filter" />
      <Button content="Add" @click="add" />
    </div>
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
          <tr v-for="(user, index) in users" :key="user.id">
            <td>
              <span class="username">{{ user.firstname }} {{ user.lastname }}</span><br>
              <span>{{ user.email }}</span>
            </td>
            <td>{{ new Date(user.created).toLocaleDateString("de-DE") }}</td>
            <td>
              <div v-if="user.roles && user.roles.length > 0">
                <v-chip v-if="user.roles[0]">
                  {{ user.roles[0] }}
                </v-chip>
                <span v-if="user.roles.length > 1" class="text-grey text-caption">
                  (+{{ user.roles.length - 1 }} others)
                </span>
              </div>
            </td>
            <td>
              <v-dialog v-model="dialogs[index].showOuterDialog" width="275">
                <template v-slot:activator="{ on, attrs }">
                  <button class="btn-more" v-on="on" v-bind="attrs"><svg-icon type="mdi" :path="path"></svg-icon></button>
                </template>
                <v-card class="popup-window">
                  <v-card-title>
                    {{ user.firstname }} {{ user.lastname }}
                  </v-card-title>
                  <v-card-text class="text-center">
                    <v-btn elevation="0" class="action-btn" @click="remove(user.id)">Update</v-btn>
                    <v-btn elevation="0" class="action-btn delete-btn" @click="openInnerDialog(index)">Delete</v-btn>
                  </v-card-text>
                </v-card>
              </v-dialog>
              <v-dialog v-model="dialogs[index].showInnerDialog" width="275">
                <v-card class="popup-window">
                  <v-card-text>
                    Confirm delete user: {{ user.firstname }} {{ user.lastname }}?
                  </v-card-text>
                  <v-card-actions>
                    <v-btn text @click="deleteUser(user.id, index)">Confirm</v-btn>
                    <v-btn text @click="closeInnerDialog(index)">Cancel</v-btn>
                  </v-card-actions>
                </v-card>
              </v-dialog>
            </td>
          </tr>
        </tbody>
      </v-table>
    </div>
  </v-container>
</template>

<script setup>
import axios from 'axios';
import InputField from '@/components/Input.vue';
import Button from '@/components/Button-Settings.vue';
import SvgIcon from '@jamescoyle/vue-icon';
import { mdiDotsHorizontal } from '@mdi/js';
</script>

<script>
export default {
  components: {
    SvgIcon,
    InputField,
    Button,
  },
  data() {
    return {
      users: [],
      dialogs: [],
      path: mdiDotsHorizontal,
    };
  },
  async mounted() {
    try {
      const response = await axios.get('users/all');
      this.users = response.data;
      this.users.forEach(user => {
          if (user.roles && user.roles.length > 0) {
            user.roles = user.roles.map(role => 
                role.charAt(0).toUpperCase() + role.slice(1).toLowerCase()
              );
          }
      });
      this.dialogs = this.users.map(() => ({ showOuterDialog: false, showInnerDialog: false }));
    } catch (e) {
      alert(e);
    }
  },
  methods: {
    add() {
      this.$router.push({ name: 'Add' });
    },
    async remove(id) {
      // Placeholder for update functionality
      console.log('Update user with id:', id);
    },
    async deleteUser(id, index) {
      try {
        const response = await axios.put(`users/delete/${id}`);
        if (response.data === 'OK') {
          this.users.splice(index, 1);
          this.dialogs.splice(index, 1);
        } else {
          console.error('Error deleting user:', response.data);
        }
      } catch (e) {
        alert(e);
      }
    },
    openInnerDialog(index) {
      this.dialogs[index].showInnerDialog = true;
    },
    closeInnerDialog(index) {
      this.dialogs[index].showInnerDialog = false;
    },
  },
};
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
    padding: 2px;
  }

  .inputfield {
    display: inline-block;
  }

  .menu {
    border: solid 1px #707070;
    border-radius: 10px;
    margin-right: 10px;
  }

  .action-btn {
    background: white;
    border: 1px black solid;
    margin: 5px;
    border-radius: 20px;
  }

  .delete-btn {
    background: black;
    color: white;
  }

  .popup-window {
    border-radius: 20px !important;
  }
</style>